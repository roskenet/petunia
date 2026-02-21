import type { NextConfig } from "next";
import { config } from 'dotenv';

// Environment configuration types and constants
type EnvironmentType = 'minikube' | 'production';
const ENV_MINIKUBE: EnvironmentType = 'minikube';
const ENV_CONFIG_FILES = {
  [ENV_MINIKUBE]: '.env.minikube'
} as const;

// Load environment configuration
const loadEnvironmentConfig = () => {
  const currentEnv = process.env.APP_ENV as EnvironmentType;

  if (currentEnv === ENV_MINIKUBE) {
    return config({
      path: ENV_CONFIG_FILES[ENV_MINIKUBE],
      override: true
    }).parsed || {};
  }

  return {};
};
const nextConfig: NextConfig = {
  env: loadEnvironmentConfig(),
  reactStrictMode: true,
  output: 'export',
  trailingSlash: true, // wichtig für nginx oder andere static hosts
  images: {
    unoptimized: true, // falls du das Next.js image optimization nicht brauchst
  },
};

export default nextConfig;