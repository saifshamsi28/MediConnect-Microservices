// IndexedDB utilities for form data caching
const DB_NAME = 'MediConnectDB';
const STORE_NAME = 'formCache';
const DB_VERSION = 1;

let db = null;

const initDB = async () => {
  if (db) return db;

  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION);

    request.onerror = () => reject(request.error);
    request.onsuccess = () => {
      db = request.result;
      resolve(db);
    };

    request.onupgradeneeded = (event) => {
      const database = event.target.result;
      if (!database.objectStoreNames.contains(STORE_NAME)) {
        database.createObjectStore(STORE_NAME, { keyPath: 'id' });
      }
    };
  });
};

export const saveFormData = async (key, data) => {
  try {
    const database = await initDB();
    const transaction = database.transaction(STORE_NAME, 'readwrite');
    const store = transaction.objectStore(STORE_NAME);
    
    await store.put({ id: key, data, timestamp: Date.now() });
  } catch (error) {
    console.error('Error saving form data:', error);
  }
};

export const loadFormData = async (key) => {
  try {
    const database = await initDB();
    const transaction = database.transaction(STORE_NAME, 'readonly');
    const store = transaction.objectStore(STORE_NAME);
    
    return new Promise((resolve, reject) => {
      const request = store.get(key);
      request.onerror = () => reject(request.error);
      request.onsuccess = () => {
        const result = request.result;
        resolve(result ? result.data : null);
      };
    });
  } catch (error) {
    console.error('Error loading form data:', error);
    return null;
  }
};

export const clearFormData = async (key) => {
  try {
    const database = await initDB();
    const transaction = database.transaction(STORE_NAME, 'readwrite');
    const store = transaction.objectStore(STORE_NAME);
    
    await store.delete(key);
  } catch (error) {
    console.error('Error clearing form data:', error);
  }
};

export const getAllFormData = async () => {
  try {
    const database = await initDB();
    const transaction = database.transaction(STORE_NAME, 'readonly');
    const store = transaction.objectStore(STORE_NAME);
    
    return new Promise((resolve, reject) => {
      const request = store.getAll();
      request.onerror = () => reject(request.error);
      request.onsuccess = () => resolve(request.result);
    });
  } catch (error) {
    console.error('Error getting all form data:', error);
    return [];
  }
};
