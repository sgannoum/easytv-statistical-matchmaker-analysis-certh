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
RUN echo "#!/bin/bash" > start_STMM_Job.sh
RUN echo "cd /root/STMM" >> start_STMM_Job.sh
RUN echo "/opt/jdk/bin/java -Xms1024m -Xmx2048m -jar /root/STMM/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar" >> start_STMM_Job.sh
RUN chmod 777 start_STMM_Job.sh

#Create Crontab file with job to run every day at 00:00
RUN echo "0 0 * * * /root/STMM/start_STMM_Job.sh" > cronJob.txt
RUN crontab cronJob.txt

#Show crond output
CMD ["crond", "-f"]

#docker build --tag stmm:v1 .
#docker image save -o C:\Users\salgan\Desktop\Docker_Images\stmm_v1.tar stmm:v1
# docker run -it --rm anapsix/alpine-java java -version