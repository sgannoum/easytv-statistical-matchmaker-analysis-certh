FROM anapsix/alpine-java:latest

# Copy War file 
RUN mkdir /STMM

# Change working directory
WORKDIR /STMM

# Copy jar files
COPY ./target/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT.jar .
COPY ./target/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar .

# Copy config.ini file 
COPY ./config.ini .

# Run container
CMD ["java", "-jar", "EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar", "-o", "/profiles"]

# docker run -it --rm anapsix/alpine-java java -version