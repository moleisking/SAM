var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('name', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('description', {
        validations: {
            presence: {
                message: 'Description is required!'
            }
        }
    });

module.exports = profile;