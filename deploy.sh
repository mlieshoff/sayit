mvn clean install
cp target/ROOT.war ../sayit-deploy/webapps
cd ../sayit-deploy
git add webapps
git commit -m "new version" .
git push
cd ../sayit