import { Component, OnInit } from "@angular/core";

import { CategoryModel } from "../models/category";
import { CategoriesService } from "../services/categories";
import { TranslateService } from "ng2-translate";

@Component({
    selector: "categories-component",
    templateUrl: "../../views/categories.html",
    styleUrls: ["../../styles/categories.css"]
})

export class Categories implements OnInit {

    private cats: CategoryModel[];
    private message: string;

    constructor(
        private cat: CategoriesService,
        private trans: TranslateService
    ) {
        this.message = "";
    }

    getCategories() {
        this.cat.all().subscribe(
            c => this.cats = c,
            error => this.message = <any>error,
            () => this.trans.get("DoneAllCategoriesPage").subscribe((res: string) => console.log(res))
        );
    }

    ngOnInit() {
        this.getCategories();
    }
}
