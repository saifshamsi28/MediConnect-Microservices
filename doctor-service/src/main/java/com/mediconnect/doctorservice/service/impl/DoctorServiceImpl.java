package com.mediconnect.doctorservice.service.impl;

import com.mediconnect.doctorservice.dto.requestDtos.DoctorRequest;
import com.mediconnect.doctorservice.dto.requestDtos.EducationRequest;
import com.mediconnect.doctorservice.dto.requestDtos.ExperienceRequest;
import com.mediconnect.doctorservice.dto.responseDtos.*;
import com.mediconnect.doctorservice.entity.*;
import com.mediconnect.doctorservice.exception.DoctorAlreadyExistsException;
import com.mediconnect.doctorservice.exception.DoctorNotFoundException;
import com.mediconnect.doctorservice.exception.InvalidRequestException;
import com.mediconnect.doctorservice.repository.DoctorAvailabilityRepository;
import com.mediconnect.doctorservice.repository.DoctorEducationRepository;
import com.mediconnect.doctorservice.repository.DoctorExperienceRepository;
import com.mediconnect.doctorservice.repository.DoctorRepository;
import com.mediconnect.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

        private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);
        private final DoctorRepository doctorRepository;
        private final DoctorEducationRepository educationRepository;
        private final DoctorExperienceRepository experienceRepository;
        private final DoctorAvailabilityRepository availabilityRepository;

        @Override
        public ApiResponse<DoctorResponse> createDoctor(DoctorRequest request) {

                if (doctorRepository.existsByEmail(request.getEmail())) {
                        throw new DoctorAlreadyExistsException(
                                        "Doctor with email '" + request.getEmail() + "' already exists!");
                }

                Doctor doctor = Doctor.builder()
                                .userId(request.getUserId())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .primarySpecialization(request.getPrimarySpecialization())
                                .active(request.isActive())
                                .dateOfJoining(request.getDateOfJoining())
                                .build();

                Doctor savedDoctor = doctorRepository.save(doctor);
                return ApiResponse.<DoctorResponse>builder()
                                .data(toDoctorResponse(savedDoctor))
                                .success(true)
                                .message("Doctor created successfully")
                                .meta(null)
                                .build();
        }

        @Override
        public ApiResponse<List<DoctorResponse>> createDoctorsBulk(List<DoctorRequest> requests) {

                List<Doctor> doctors = requests.stream().map(req -> {

                        if (doctorRepository.existsByEmail(req.getEmail())) {
                                throw new DoctorAlreadyExistsException(
                                                "Duplicate email found: " + req.getEmail());
                        }

                        return Doctor.builder()
                                        .userId(req.getUserId())
                                        .firstName(req.getFirstName())
                                        .lastName(req.getLastName())
                                        .email(req.getEmail())
                                        .primarySpecialization(req.getPrimarySpecialization())
                                        .active(req.isActive())
                                        .dateOfJoining(req.getDateOfJoining())
                                        .build();

                }).collect(Collectors.toList());

                List<Doctor> savedDoctors = doctorRepository.saveAll(doctors);

                List<DoctorResponse> doctorResponses = savedDoctors.stream()
                                .map(this::toDoctorResponse)
                                .toList();
                Meta meta = Meta.builder()
                                .matched(savedDoctors.size())
                                .returned(doctorResponses.size())
                                .page(0)
                                .size(10)
                                .totalPages(1)
                                .sortBy(doctorResponses.getFirst().getPrimarySpecialization())
                                .order(doctorResponses.getFirst().getFirstName()
                                                + doctorResponses.getFirst().getLastName())
                                .active(doctorResponses.getFirst().isActive())
                                .build();

                return ApiResponse.<List<DoctorResponse>>builder()
                                .data(doctorResponses)
                                .meta(meta)
                                .success(true)
                                .message("Doctor added successfully")
                                .build();

        }

        @Override
        public ApiResponse<List<DoctorResponse>> getDoctorsByFilters(
                        Boolean active,
                        String name,
                        int page,
                        int size,
                        String sortBy,
                        String order) {

                Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

                Pageable pageable = PageRequest.of(page, size, sort);

                Page<Doctor> doctorPage;

                // Search by name and active status
                if (name != null && !name.trim().isEmpty()) {
                        if (active == null) {
                                doctorPage = doctorRepository
                                                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                                                name, name, pageable);
                        } else {
                                doctorPage = doctorRepository
                                                .findByActiveAndFirstNameContainingIgnoreCaseOrActiveAndLastNameContainingIgnoreCase(
                                                                active, name, active, name, pageable);
                        }
                } else {
                        // Original logic when no name search
                        if (active == null) {
                                doctorPage = doctorRepository.findAll(pageable);
                        } else {
                                doctorPage = doctorRepository.findByActive(active, pageable);
                        }
                }

                List<DoctorResponse> responses = doctorPage.getContent()
                                .stream()
                                .map(this::toDoctorResponse)
                                .toList();

                // log.info("Doctor service: No of doctors fetched: {}",responses.size());

                Meta meta = Meta.builder()
                                .matched((int) doctorPage.getTotalElements())
                                .returned(responses.size())
                                .page(page)
                                .size(size)
                                .totalPages(doctorPage.getTotalPages())
                                .sortBy(sortBy)
                                .order(order)
                                .active(active)
                                .build();

                return ApiResponse.<List<DoctorResponse>>builder()
                                .success(true)
                                .message("Doctors retrieved successfully")
                                .data(responses)
                                .meta(meta)
                                .build();
        }

        @Override
        public ApiResponse<DoctorResponse> getDoctorById(UUID doctorId) {

                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));
                // log.info("fetched doctor: {}", doctor);

                Meta meta = Meta.builder()
                                .matched(1)
                                .returned(1)
                                .page(0)
                                .size(1)
                                .totalPages(1)
                                .sortBy(null)
                                .order(null)
                                .active(doctor.isActive())
                                .build();

                return ApiResponse.<DoctorResponse>builder()
                                .data(toDoctorResponse(doctor))
                                .success(true)
                                .message("Doctor retrieved successfully")
                                .meta(meta)
                                .build();
        }

        @Override
        public ApiResponse<String> deleteDoctorById(UUID doctorId) {
                if (!doctorRepository.existsById(doctorId)) {
                        throw new DoctorNotFoundException("No doctor found with: " + doctorId);
                }

                doctorRepository.deleteById(doctorId);
                return ApiResponse.<String>builder()
                                .success(true)
                                .message("Doctor deleted successfully")
                                .data("Deleted")
                                .build();
        }

        @Override
        public ApiResponse<DoctorResponse> getDoctorByUserId(UUID userId) {
                Doctor doctor = doctorRepository.findByUserId(userId)
                                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found for user: " + userId));

                return ApiResponse.<DoctorResponse>builder()
                                .data(toDoctorResponse(doctor))
                                .success(true)
                                .message("Doctor retrieved successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<DoctorResponse> updateDoctor(UUID doctorId, DoctorRequest request,
                        UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to update this doctor profile");
                }

                // Update fields
                if (request.getPrimarySpecialization() != null) {
                        doctor.setPrimarySpecialization(request.getPrimarySpecialization());
                }
                if (request.getDateOfJoining() != null) {
                        doctor.setDateOfJoining(request.getDateOfJoining());
                }

                Doctor updatedDoctor = doctorRepository.save(doctor);

                return ApiResponse.<DoctorResponse>builder()
                                .data(toDoctorResponse(updatedDoctor))
                                .success(true)
                                .message("Doctor profile updated successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<EducationResponse> addEducation(UUID doctorId, EducationRequest request,
                        UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                DoctorEducation education = new DoctorEducation();
                education.setDegree(request.getDegree());
                education.setInstitution(request.getInstitution());
                education.setYearOfCompletion(request.getYearOfCompletion());
                education.setDoctor(doctor);

                DoctorEducation saved = educationRepository.save(education);

                return ApiResponse.<EducationResponse>builder()
                                .data(toEducationResponse(saved))
                                .success(true)
                                .message("Education added successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<ExperienceResponse> addExperience(UUID doctorId, ExperienceRequest request,
                        UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                DoctorExperience experience = new DoctorExperience();
                experience.setHospitalName(request.getHospitalName());
                experience.setRole(request.getRole());
                experience.setYears(request.getYears());
                experience.setDoctor(doctor);

                DoctorExperience saved = experienceRepository.save(experience);

                return ApiResponse.<ExperienceResponse>builder()
                                .data(toExperienceResponse(saved))
                                .success(true)
                                .message("Experience added successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<EducationResponse> updateEducation(UUID doctorId, Long educationId, EducationRequest request,
                        UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                DoctorEducation education = educationRepository.findById(educationId)
                                .orElseThrow(() -> new InvalidRequestException("Education entry not found"));

                if (!education.getDoctor().getId().equals(doctorId)) {
                        throw new InvalidRequestException("Education entry does not belong to this doctor");
                }

                education.setDegree(request.getDegree());
                education.setInstitution(request.getInstitution());
                education.setYearOfCompletion(request.getYearOfCompletion());

                DoctorEducation saved = educationRepository.save(education);

                return ApiResponse.<EducationResponse>builder()
                                .data(toEducationResponse(saved))
                                .success(true)
                                .message("Education updated successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<ExperienceResponse> updateExperience(UUID doctorId, Long experienceId,
                        ExperienceRequest request,
                        UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                DoctorExperience experience = experienceRepository.findById(experienceId)
                                .orElseThrow(() -> new InvalidRequestException("Experience entry not found"));

                if (!experience.getDoctor().getId().equals(doctorId)) {
                        throw new InvalidRequestException("Experience entry does not belong to this doctor");
                }

                experience.setHospitalName(request.getHospitalName());
                experience.setRole(request.getRole());
                experience.setYears(request.getYears());

                DoctorExperience saved = experienceRepository.save(experience);

                return ApiResponse.<ExperienceResponse>builder()
                                .data(toExperienceResponse(saved))
                                .success(true)
                                .message("Experience updated successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<String> removeEducation(UUID doctorId, Long educationId, UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                educationRepository.deleteByDoctorIdAndId(doctorId, educationId);

                return ApiResponse.<String>builder()
                                .data("Deleted")
                                .success(true)
                                .message("Education removed successfully")
                                .meta(null)
                                .build();
        }

        @Override
        @Transactional
        public ApiResponse<String> removeExperience(UUID doctorId, Long experienceId, UUID authenticatedUserId) {
                Doctor doctor = doctorRepository.findById(doctorId)
                                .orElseThrow(() -> new DoctorNotFoundException(
                                                "Doctor not found with id: " + doctorId));

                // Validate ownership
                if (!doctor.getUserId().equals(authenticatedUserId)) {
                        throw new InvalidRequestException("You are not authorized to modify this doctor profile");
                }

                experienceRepository.deleteByDoctorIdAndId(doctorId, experienceId);

                return ApiResponse.<String>builder()
                                .data("Deleted")
                                .success(true)
                                .message("Experience removed successfully")
                                .meta(null)
                                .build();
        }

        private DoctorResponse toDoctorResponse(Doctor doctor) {
                // Fetch related data
                List<DoctorEducation> educationList = educationRepository.findByDoctorId(doctor.getId());
                List<DoctorExperience> experienceList = experienceRepository.findByDoctorId(doctor.getId());
                List<DoctorAvailability> availabilityList = availabilityRepository.findByDoctorId(doctor.getId());

                // Convert to response DTOs
                List<EducationResponse> educationResponses = educationList.stream()
                                .map(this::toEducationResponse)
                                .toList();

                List<ExperienceResponse> experienceResponses = experienceList.stream()
                                .map(this::toExperienceResponse)
                                .toList();

                List<AvailabilityResponse> availabilityResponses = availabilityList.stream()
                                .map(this::toAvailabilityResponse)
                                .toList();

                // Compute profile completeness
                boolean profileComplete = doctor.getPrimarySpecialization() != null &&
                                !doctor.getPrimarySpecialization().trim().isEmpty() &&
                                !educationList.isEmpty() &&
                                !experienceList.isEmpty();

                return DoctorResponse.builder()
                                .userId(doctor.getUserId())
                                .doctorId(doctor.getId())
                                .active(doctor.isActive())
                                .primarySpecialization(doctor.getPrimarySpecialization())
                                .dateOfJoining(doctor.getDateOfJoining())
                                .email(doctor.getEmail())
                                .firstName(doctor.getFirstName())
                                .lastName(doctor.getLastName())
                                .educationList(educationResponses)
                                .experienceList(experienceResponses)
                                .availabilityList(availabilityResponses)
                                .profileComplete(profileComplete)
                                .build();
        }

        private EducationResponse toEducationResponse(DoctorEducation education) {
                return EducationResponse.builder()
                                .id(education.getId())
                                .degree(education.getDegree())
                                .institution(education.getInstitution())
                                .yearOfCompletion(education.getYearOfCompletion())
                                .build();
        }

        private ExperienceResponse toExperienceResponse(DoctorExperience experience) {
                return ExperienceResponse.builder()
                                .id(experience.getId())
                                .hospitalName(experience.getHospitalName())
                                .role(experience.getRole())
                                .years(experience.getYears())
                                .build();
        }

        private AvailabilityResponse toAvailabilityResponse(DoctorAvailability availability) {
                return AvailabilityResponse.builder()
                                .availabilityId(availability.getId())
                                .dayOfWeek(availability.getDayOfWeek())
                                .startTime(availability.getStartTime())
                                .endTime(availability.getEndTime())
                                .available(availability.isAvailable())
                                .build();
        }
}
