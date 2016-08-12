var model = require('nodejs-model');

var tag = new model("Tag")
    .attr('id', {
        validations: {
            presence: {
                message: 'ID tag is required!'
            }
        }
    })
    .attr('text', {
        validations: {
            presence: {
                message: 'Name tag is required!'
            }
        }
    });

module.exports = tag;