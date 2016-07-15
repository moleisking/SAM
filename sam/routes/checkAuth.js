var config = require('../config/settings');
var jwt = require('jwt-simple');

var getToken = function (headers) {
  if (headers && headers.authorization) {
    var parted = headers.authorization.split(' ');
    if (parted.length === 2)
      return parted[1];
    else
      return null;
  } else
    return null;
};

var isAuthenticated = function (req, res, next) {
  var token = getToken(req.headers);
  if (token) {
    var decoded = jwt.decode(token, config.secret);
    if (!decoded)
      return res.status(403).send({ success: false, message: 'Token decoded failed.' });
    else {
      req.decoded = decoded;
      next();
    }
  } else
    return res.status(403).send({ success: false, message: 'No token provided.' });
};

module.exports = isAuthenticated;
