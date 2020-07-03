# EasyTV Statistical Matchmaker: Analysis Component 

## Installation instructions

First, get the code by running the following command in a terminal window:

    git clone git@github.com:sgannoum/easytv-statistical-matchmaker-analysis-certh.git

Then change to the newly created directory and run the following command:

    mvn install
	
## Running the Statistical Matchmaker

First, you must declare the following environmental variables:

| variable | comment |
| ------ | ------ |
| STMM_HOST | Statistical matchmaker runtime component host |
| STMM_PORT | Statistical matchmaker runtime component port |
| RBMM_HOST | Rule based matchmaker component host |
| RBMM_PORT |Rule based matchmaker component port |
| DB_HOST | Users' profile database host |
| DB_PORT | Users' profile database port |
| DB_NAME | Users' profile database name |
| DB_USER | Users' profile database use |
| DB_PASSWORD | Users' profile database password |
  
Then you can run the following command: 

    java -jar /target/EASYTV_STMM_profile_generator-0.0.1-SNAPSHOT-jar-with-dependencies.jar -e production
		
## Docker image

You can build a docker image from the docker file contained in the statistical matchmaker by running the following command:

	docker build --tag stmm:v1 .

## License

The code of the EasyTV statistical Matchmaker's analysis component is available under the [Apache 2.0 license](https://github.com/sgannoum/easytv-statistical-matchmaker-analysis-certh/blob/master/LICENSE.txt).

## Funding Acknowledgement

The research leading to these results has received funding from
the European Union's H2020-ICT-2016-2, ICT-19-2017 Media and content convergence
under grant agreement no. 761999.