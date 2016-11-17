var model = require("nodejs-model");

var category = new model("Category")
    .attr("id", {
        validations: {
            presence: {
                message: "ID is required!"
            }
        }
    })
    .attr("text", {
        validations: {
            presence: {
                message: "Name is required!"
            }
        }
    })
    .attr("description")
    .attr("cat");

module.exports = category;