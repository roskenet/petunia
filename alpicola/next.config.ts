import type { NextConfig } from "next";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      // API Requests
      {
        source: "/api/:path*",
        destination: `${API_BASE_URL}/api/:path*`,
      },
      // OAuth2 Login Endpoint
      {
        source: "/oauth2/:path*",
        destination: `${API_BASE_URL}/oauth2/:path*`,
      },
      // Spring Security Login Redirect Handler
      {
        source: "/login/:path*",
        destination: `${API_BASE_URL}/login/:path*`,
      },
      // Logout
      {
        source: "/logout",
        destination: `${API_BASE_URL}/logout`,
      },
      // Eventuell CSRF Token etc.
      {
        source: "/error",
        destination: `${API_BASE_URL}/error`,
      },
    ];
  },
};

export default nextConfig;
