# Main

Collaborators : Chiffelle Janis, Tomic Mario

# CI/CD

The 2 micro-services use CI/CD for symplifing the developpmeent process. On push on the dev branch, a docker image will be created and put on the docker registery of github. With this, we have all the differents version of the images in a centralized place. We don't have a CI/CD on the main yet, we will have one when will push on the aws EC2.

# Run the micros-services

The dockers images are hosted at the following addresses :
- https://github.com/AMT-TEAM01/DataObjectAWS/pkgs/container/data-object-aws
- https://github.com/AMT-TEAM01/LabelDetectorAWS/pkgs/container/label-detector-aws

## Procedures

You first need to authenticate at the container registery with the following [link](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry) under the section section "Authenticating to the Container registry". 

After you have done step 1,2,3 of the link. You can pull the image with the command <code>docker pull ghcr.io/amt-team01/data-object-aws:latest</code>, that will download the image for the communication with s3 on our machine.

We can then launch our docker with the following command <code>docker run --name=doc -p 8080:8080 -e AWS_ACCESS_KEY_ID=XXXXXXXXXXXX -e AWS_SECRET_ACCESS_KEY=XXXXXXXXXXXX -e AWS_DEFAULT_REGION=XXXXXXXXX ghcr.io/amt-team01/data-object-aws</code> by replacing the XXXX with the correct inputs.

Finally we can communicate with the docker with for example the following curl command <code>curl -X POST -d data=02 -d remote=test.txt localhost:8080/object</code>

# Getting ready to work on the project

This project uses Maven, java, and is developped using Intellij Ultimate Edition. Please, ensure that you have them installed.

## Opening the projects

Simply clone the repos, and then open the project folder containing the pom.xml in Intellij. A prompt will appear to propose you to import the Maven project. Accept it.

## Resolving the dependencies

Execute the following command, where the pom.xml is located

`mvn dependency:resolve`

Maven will then proceed to download all the dependencies listed in the pom.xml file. 

## Compiling and running

To compile the project and generate an executable file, run `mvn package` 

To run the project:

### Windows

Execute the `run_win.bat` script, at the root of the project (where pom.xml is located)

### Linux

1. Give execution right to the script with `chmod +x run_linux.sh`, at the root of the project (where pom.xml is located)
2. Execute the script with `./run_linux` 
