var model = require('nodejs-model');

var category = new model("Category")
    .attr('id', {
        validations: {
            presence: {
                message: 'ID is required!'
            }
        }
    })
    .attr('name', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('description');

module.exports = category;