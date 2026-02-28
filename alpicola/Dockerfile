# Build Stage – nur um static site zu exportieren
FROM node:20-alpine AS builder
WORKDIR /app

# Build argument to determine what to build
ARG BUILD_ENV=production

# Nur das Nötigste kopieren
COPY package.json package-lock.json ./
RUN npm ci

COPY . ./
# Run the appropriate build command based on the build environment
RUN if [ "$BUILD_ENV" = "minikube" ]; then \
        npm run build:minikube; \
    else \
        npm run build; \
    fi

# NGINX Stage – statische Seite servieren
FROM nginx:stable-alpine AS runner

# Kopiere deine exportierte Site nach /usr/share/nginx/html
COPY --from=builder /app/out /usr/share/nginx/html

# Optional: eigene nginx.conf einspielen (siehe unten)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
