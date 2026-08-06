export type ApiResponse<T> = {
  success: true;
  message: string;
  data: T;
  timestamp: string;
};

export type ApiError = {
  success: false;
  status: number;
  error: string;
  code: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
  timestamp: string;
};

export type ApiResult<T> = ApiResponse<T> | ApiError;

export type PageRequest = {
  page?: number;
  size?: number;
  sort?: string;
};

export type PageResponse<T> = {
  items: T[];
  page: number;
  size: number;
  totalItems: number;
  totalPages: number;
};
