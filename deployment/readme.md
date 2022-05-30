# Docker container deployment in Google cloud by using YAML config file

## Tutorial
Video tutorial - [Youtube](https://youtu.be/ZHY8Zd4R874)

## YAML generator
Brandon Potter's YML builder - [https://static.brandonpotter.com/kubernetes/DeploymentBuilder.html](https://static.brandonpotter.com/kubernetes/DeploymentBuilder.html)

### Commands
- kubectl apply -f yourconfig.yaml

## Steps
- first deploy(expose) firebase service by using yaml config.
- get your external ip of firebase service.
- replace external ip of firebase service instead of firebase.service (http://localhost:8085/user/all -> http://xx.xx.xxx.xxx/user/all).
- deploy(expose) driver service by using yaml config.
