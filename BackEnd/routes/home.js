var express = require('express');
var router = express.Router();

router.get('/', function (req, res, next) {
  res.json({ root: 'Welcome to SAM.' });
});

module.exports = router;
