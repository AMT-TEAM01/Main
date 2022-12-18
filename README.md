# Main

# Lancer les micros-services

Les images docker des micro services sont heberger aux addresses suivante :
- https://github.com/AMT-TEAM01/DataObjectAWS/pkgs/container/data-object-aws
- https://github.com/AMT-TEAM01/LabelDetectorAWS/pkgs/container/label-detector-aws

## Marche a suivre

Il faut d'abors s'authentifier sur le Container registery, pour ce faire consulter le [lien](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry) sous la section "Authenticating to the Container registry". 

Une foit l'étape 1, 2 et 3 du lien effectuer. On peut pull l'image avec la commande <code>docker pull ghcr.io/amt-team01/data-object-aws:latest</code>, se qui va download l'image pour la communication avec s3 sur notre machine.

On peut ensuite lancer le docker avec la commande suivante <code>docker run --name=doc -p 8080:8080 -e AWS_ACCESS_KEY_ID=XXXXXXXXXXXX -e AWS_SECRET_ACCESS_KEY=XXXXXXXXXXXX -e AWS_DEFAULT_REGION=XXXXXXXXX ghcr.io/amt-team01/data-object-aws</code> en remplacant les XXXX avec les bons inputs.

On peut ensuite communiquer directement avec le conteneur avec par exemple curl avec la commande suivante <code>curl -X POST -d data=02 -d remote=test.txt localhost:8080/object</code>
