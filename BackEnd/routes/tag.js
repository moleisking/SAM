var express = require("express");
var router = express.Router();
var tag = require("../dal/tag");
var util = require("../util/util");
var passport = require("passport");
var Localize = require("localize");
var myLocals = new Localize("localizations/user");

router.get("/gettags", function (req, res, next) {
     console.log("route tag get called");
    tag.all(function (err, data) {
        if (err) {
             console.log("route tags error");
            return res.status(500).json({ err });
        }
            
        if (data.length === 0) {
             console.log("route tags none");
            return res.status(404).json({ "tags": data });
        }
          console.log(data);  
        res.json({ tags: data });
    });
});

module.exports = router;