# === STAGE 1 Dependencies installieren ===
FROM node:20-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

# === STAGE 2: App bauen ===
FROM node:20-alpine AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build

# === STAGE 3: Produktion starten ===
FROM node:20-alpine AS runner
WORKDIR /app

ENV NODE_ENV=production

# Nur das Nötigste übernehmen
COPY --from=builder /app/public ./public
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./

EXPOSE 3000

CMD ["npm", "start"]
