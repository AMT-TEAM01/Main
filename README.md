# Main

Collaborators : Chiffelle Janis, Tomic Mario

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

# Run the main

