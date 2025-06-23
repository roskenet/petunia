import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      // API Requests
      {
        source: "/api/:path*",
        destination: "http://localhost:8080/api/:path*",
      },
      // OAuth2 Login Endpoint
      {
        source: "/oauth2/:path*",
        destination: "http://localhost:8080/oauth2/:path*",
      },
      // Spring Security Login Redirect Handler
      {
        source: "/login/:path*",
        destination: "http://localhost:8080/login/:path*",
      },
      // Logout
      {
        source: "/logout",
        destination: "http://localhost:8080/logout",
      },
      // Eventuell CSRF Token etc.
      {
        source: "/error",
        destination: "http://localhost:8080/error",
      },
    ];
  },
};

export default nextConfig;
