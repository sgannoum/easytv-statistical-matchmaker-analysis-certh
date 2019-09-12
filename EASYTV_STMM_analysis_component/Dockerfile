FROM anapsix/alpine-java:latest

# Copy War file 
RUN mkdir /root/STMM

# Change working directory
WORKDIR /root/STMM

# Copy jar files
COPY ./target/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT.jar .
COPY ./target/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar .

# Copy config.ini file 
COPY ./config.ini .

# Copy sh file
RUN echo "#!/bin/sh" > start_STMM_Job.sh
RUN echo "cd /root/STMM" >> start_STMM_Job.sh
RUN echo "/opt/jdk/bin/java -jar /root/STMM/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar" >> start_STMM_Job.sh
RUN chmod 777 start_STMM_Job.sh

#Create Crontab file
RUN echo "* * * * * /root/STMM/start_STMM_Job.sh" > cornJob.txt

#add Crontab file
RUN crontab cornJob.txt

# Run container
#CMD ["java", "-jar", "EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar"]

# docker run -it --rm anapsix/alpine-java java -version