var express = require('express');
var router = express.Router();
var user = require("../core/user.core");

router.get('/', function (req, res, next) {
    user.all(function (err, data) {
        if (err)
            return next(err);
        else
            res.render('user/list', { data: data, greeting: res.__('Hello') });
    });
});

router.get('/api/all', function (req, res, next) {
    res.setHeader('Content-Type', 'application/json');
    user.all(function (err, data) {
        if (err)
            res.json(err);
        else
            res.json(data);
    });
});

module.exports = router;