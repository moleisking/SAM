var fs = require('fs');
const path = './src/app/config/';

fs.unlink(path + 'settings.ts', (err) => {
  if (err) throw err;
  fs.createReadStream(path + 'settingsDev.ts').pipe(fs.createWriteStream(path + 'settings.ts'));
});
