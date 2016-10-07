import { Component, OnInit } from "@angular/core";

import { CategoryModel } from "../models/category";
import { CategoriesService } from "../services/categories";

@Component({
    selector: "categories-component",
    templateUrl: "../../views/categories.html"
})

export class Categories implements OnInit {

    private cats: CategoryModel[];
    private message: string;

    constructor(private cat: CategoriesService) {
        this.message = "Categories Text";
    }

    getCategories() {
        this.cat.all().subscribe(
            c => this.cats = c,
            error => this.message = <any>error,
            () => console.log("Done get all categories page.")
        );
    }

    ngOnInit() {
        this.getCategories();
    }
}
