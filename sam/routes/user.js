var express = require('express');
var router = express.Router();
var user = require("../core/user.core");

router.get('/:id', function (req, res, next) {
    var id = req.params.id;
    user.read(id, function (err, data) {
        if (err)
            return next(err);
        else
            res.render('user/user', data);
    });
});

router.get('/del/:id', function (req, res, next) {
    var id = req.params.id;
    user.delete(id, function (err, data) {
        if (err)
            return next(err);
        else
            res.redirect("/users")
    });
});

module.exports = router;