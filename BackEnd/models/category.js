var model = require('nodejs-model');

var category = new model("Category")
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

module.exports = category;