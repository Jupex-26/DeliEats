# ==========================================
# Etapa 1: Compilación de la aplicación
# ==========================================
FROM node:22-alpine AS build

# Definimos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos package.json y package-lock.json para instalar dependencias primero
# Esto aprovecha el caché de capas de Docker si las dependencias no cambian
COPY package*.json ./

# Instalamos todas las dependencias (más tolerante con el lock file)
RUN npm install

# Copiamos todo el código fuente del proyecto
COPY . .

# Compilamos la aplicación de Ionic + Angular + Tailwind para producción
RUN npm run build

# ==========================================
# Etapa 2: Servidor Web de Producción (Nginx)
# ==========================================
FROM nginx:alpine

# Copiamos la configuración personalizada de Nginx para soportar enrutamiento SPA (Angular)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiamos los archivos compilados en la Etapa 1 hacia la carpeta pública de Nginx
# Angular 17+ con el builder de aplicaciones exporta por defecto a dist/<nombre-app>/browser
COPY --from=build /app/dist/app/browser /usr/share/nginx/html

# Exponemos el puerto 80 para el tráfico HTTP
EXPOSE 80

# Iniciamos Nginx en primer plano (foreground)
CMD ["nginx", "-g", "daemon off;"]
