import axios, { type AxiosRequestConfig } from "axios";

// Internal axios instance
const _axiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || "https://project-xeno.onrender.com/api/v1",
  headers: {
    "X-API-KEY": process.env.NEXT_PUBLIC_API_KEY || "",
    "Content-Type": "application/json",
  },
});

/**
 * Normalises the backend envelope:
 *   { success: true, data: <actual payload>, message: "...", pagination: {...} }
 * into just the <actual payload>. The `pagination` field (totalPages, totalElements, etc.)
 * is preserved by attaching it as `_pagination` on the returned value so pages can use it.
 * If the backend returns a raw body (no envelope), that body is returned as-is.
 */
function unwrap(body: any): any {
  if (body && typeof body === "object" && Object.prototype.hasOwnProperty.call(body, "success")) {
    if (!body.success) {
      throw new Error(body.errorMessage || body.message || "API request failed");
    }
    const payload = body.data !== undefined ? body.data : body;
    // Attach pagination metadata so components can read totalPages / totalElements
    if (body.pagination && payload !== null && payload !== undefined) {
      try {
        if (Array.isArray(payload)) {
          // Arrays: attach as non-enumerable property
          Object.defineProperty(payload, "_pagination", {
            value: body.pagination,
            writable: true,
            enumerable: false,
            configurable: true,
          });
        } else if (typeof payload === "object") {
          payload._pagination = body.pagination;
        }
      } catch (_) { /* ignore if frozen */ }
    }
    return payload;
  }
  return body;
}

async function request(config: AxiosRequestConfig): Promise<any> {
  try {
    const response = await _axiosInstance.request(config);
    return unwrap(response.data);
  } catch (error: any) {
    if (error?.response) {
      // Server responded with error status
      const body = error.response.data;
      const message =
        (body && (body.errorMessage || body.message)) ||
        `HTTP ${error.response.status}`;
      console.error("[API Error]", error.response.status, config.url, body);
      throw new Error(message);
    }
    throw error;
  }
}

/** Typed API client — all methods return `Promise<any>` so components can use data freely */
export const api = {
  get: (url: string, config?: AxiosRequestConfig) =>
    request({ ...config, method: "GET", url }),

  post: (url: string, data?: unknown, config?: AxiosRequestConfig) =>
    request({ ...config, method: "POST", url, data }),

  put: (url: string, data?: unknown, config?: AxiosRequestConfig) =>
    request({ ...config, method: "PUT", url, data }),

  patch: (url: string, data?: unknown, config?: AxiosRequestConfig) =>
    request({ ...config, method: "PATCH", url, data }),

  delete: (url: string, config?: AxiosRequestConfig) =>
    request({ ...config, method: "DELETE", url }),
};
