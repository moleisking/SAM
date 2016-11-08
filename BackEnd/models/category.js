var model = require('nodejs-model');

var category = new model("Category")
    .attr('id', {
        validations: {
            presence: {
                message: 'ID category is required!'
            }
        }
    })
    .attr('name', {
        validations: {
            presence: {
                message: 'Category name is required!'
            }
        }
    })
    .attr('description');

module.exports = category;