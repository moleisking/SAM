# SAM-fe

## Install all packages dependencies (BTW after we delete node_modules folder)
npm i

## Clean (delete node_modules typings and dist folder) all folders and ReInstall all packages dependencies
npm run reinstall

## Clean (dist folder) and eliminate production files
npm run deletedist

## Serve the website for testing and development (don't forget run the back end first)
npm run serve

## Compile the project for production and set all files in dist folder
npm run build

## Serve the website with production files (port 3000)
npm run serveprod

## Troubleshoot preconfigured server example
Search for string presence
grep -rl "localhost" /usr/share/nginx/html
Chandge settings in compiled file
sudo sed -i 's/localhost/192.168.1.110/g' main.670b9311459a1bfde6b0.js.map
