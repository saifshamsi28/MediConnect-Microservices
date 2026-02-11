import React, { useState } from 'react';
import { CloudUpload, X } from 'lucide-react';

export const FileUploader = ({ 
  onFilesSelected, 
  acceptedTypes = 'image/*,application/pdf',
  maxSize = 5 * 1024 * 1024,
  multiple = false,
  error,
}) => {
  const [files, setFiles] = useState([]);
  const [dragActive, setDragActive] = useState(false);

  const handleFiles = (fileList) => {
    const newFiles = Array.from(fileList);
    const validFiles = newFiles.filter(file => {
      if (file.size > maxSize) {
        alert(`File ${file.name} is too large. Max size is ${maxSize / 1024 / 1024}MB`);
        return false;
      }
      return true;
    });

    if (multiple) {
      const updatedFiles = [...files, ...validFiles];
      setFiles(updatedFiles);
      onFilesSelected(updatedFiles);
    } else {
      setFiles(validFiles);
      onFilesSelected(validFiles[0]);
    }
  };

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    handleFiles(e.dataTransfer.files);
  };

  const removeFile = (index) => {
    const updatedFiles = files.filter((_, i) => i !== index);
    setFiles(updatedFiles);
    onFilesSelected(updatedFiles);
  };

  return (
    <div className="w-full">
      <div
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        className={`relative border-2 border-dashed rounded-lg p-8 text-center transition-colors ${
          dragActive 
            ? 'border-primary bg-primary/5' 
            : 'border-neutral-300 bg-neutral-50 hover:border-primary'
        } ${error ? 'border-red-500 bg-red-50' : ''}`}
      >
        <input
          type="file"
          multiple={multiple}
          accept={acceptedTypes}
          onChange={(e) => handleFiles(e.target.files)}
          className="absolute inset-0 opacity-0 cursor-pointer"
        />
        <CloudUpload className="mx-auto h-8 w-8 text-neutral-400 mb-2" />
        <p className="text-sm text-neutral-600">
          Drag and drop files here, or click to select
        </p>
        <p className="text-xs text-neutral-500 mt-1">
          Max file size: {maxSize / 1024 / 1024}MB
        </p>
      </div>

      {files.length > 0 && (
        <div className="mt-4 space-y-2">
          {files.map((file, index) => (
            <div
              key={index}
              className="flex items-center justify-between bg-neutral-50 p-3 rounded-lg border border-neutral-200"
            >
              <span className="text-sm text-neutral-700">{file.name}</span>
              <button
                type="button"
                onClick={() => removeFile(index)}
                className="text-neutral-400 hover:text-red-600 transition-colors"
              >
                <X className="h-4 w-4" />
              </button>
            </div>
          ))}
        </div>
      )}

      {error && (
        <p className="mt-2 text-sm text-red-600">{error}</p>
      )}
    </div>
  );
};

export default FileUploader;
