import { Component, OnInit } from "@angular/core";
import { CategoryModel } from "../models/category";
import { CategoriesService } from "../services/categories";

@Component({
    selector: "categories-component",
    templateUrl: "../../views/categories.html",
    providers: [CategoriesService]
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
            error => this.message = <any>error);
    }

    ngOnInit() {
        this.getCategories();
    }
}