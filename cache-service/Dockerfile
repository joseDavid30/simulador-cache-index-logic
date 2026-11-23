# Etapa 1: compilar la libreria nativa
FROM gcc:14 AS nativo
WORKDIR /src
COPY libcache.c .
RUN gcc -Wall -Wextra -O2 -shared -fPIC -o libcache.so libcache.c

# Etapa 2: compilar el JAR
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline
COPY src src
RUN ./mvnw -B -DskipTests package \
 && find target -maxdepth 1 -name "*.jar" ! -name "original-*" -exec cp {} /app/app.jar \;

# Etapa 3: imagen final (sin compilador)
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -r -u 1001 app
COPY --from=build /app/app.jar app.jar
COPY --from=nativo /src/libcache.so /app/lib/libcache.so
USER app
EXPOSE 8081
ENV JAVA_TOOL_OPTIONS="-Xmx256m -Djna.library.path=/app/lib"
ENTRYPOINT ["java", "-jar", "app.jar"]