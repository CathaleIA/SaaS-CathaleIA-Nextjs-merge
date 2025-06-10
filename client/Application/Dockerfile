# Usa Node 20 (requerido para React 19)
FROM node:20.16.1-alpine3.19

# Instalar dependencias esenciales + AWS CLI
RUN apk add --no-cache \
    curl \
    unzip \
    python3 \
    make \
    g++ \
    git \
    && curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && ./aws/install \
    && rm -rf awscliv2.zip aws

WORKDIR /app

# 1. Copiar solo lo necesario para instalar dependencias
COPY package.json package-lock.json ./

# 2. Instalar dependencias (con flags para React 19)
RUN npm install --force --legacy-peer-deps

# 3. Copiar todo el código fuente
COPY . .

# 4. Dar permisos al script de despliegue
RUN chmod +x deployment.sh
