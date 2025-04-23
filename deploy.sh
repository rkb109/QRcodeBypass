JAR_NAME=qrcode-0.0.1-SNAPSHOT.jar 
KEY_PATH=~/Desktop/qrkey.pem
EC2_USER=ubuntu
EC2_HOST=ec2-52-79-235-49.ap-northeast-2.compute.amazonaws.com
REMOTE_PATH=/home/ubuntu

scp -i $KEY_PATH build/libs/$JAR_NAME $EC2_USER@$EC2_HOST:$REMOTE_PATH/qrcode.jar
echo "jar file sent"

ssh -i $KEY_PATH $EC2_USER@$EC2_HOST <<EOF
docker rm -f qrserver || true
docker rmi image-qrcode || true
docker build -t image-qrcode .
docker run -itd -p 8090:8090 --restart unless-stopped --name qrserver image-qrcode
EOF
