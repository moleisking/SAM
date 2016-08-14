import { Component, OnInit } from "@angular/core";
import { REACTIVE_FORM_DIRECTIVES, FormGroup, FormBuilder } from "@angular/forms";
import { Validators, Control, ControlGroup } from "@angular/common";
import { Router } from "@angular/router";
import { SELECT_DIRECTIVES } from "ng2-select";
import { CategoriesService } from "../services/categories";
import { WorkService } from "../services/work";
import { Category } from "../models/category";
import { Tag } from "../models/tag";

@Component({
    selector: "addawork-form-component",
    templateUrl: "/views/addawork-form.html",
    // styleUrls: ["../styles/ng2-select.css"], // WARNING: find out why i cant set the styles here and can on the index file.
    styleUrls: ["/styles/form.css"],
    directives: [SELECT_DIRECTIVES, REACTIVE_FORM_DIRECTIVES],
    providers: [CategoriesService]
})

export class AddAWorkFormComponent implements OnInit {

    private cats: Array<Category>;
    private tags: Array<Tag>;

    private tagsValue: any = [];

    private myForm: FormGroup; // our model driven form
    private submitted: boolean; // keep track on form submission
    private message: string;
    private areTagsAvailable: boolean = false;

    constructor(private formBuilder: FormBuilder, private cat: CategoriesService, private work: WorkService,
        private router: Router) {
        this.message = "Add a work messages here.";
    }

    ngOnInit() {
        let regexPatterns = { numbers: "^[0-9]*$" };
        this.myForm = this.formBuilder.group({
            name: ["", <any>Validators.required],
            description: ["", <any>Validators.required],
            categories: ["",
                Validators.compose([
                    Validators.pattern(regexPatterns.numbers),
                    Validators.required
                ])],
            tags: [""]
        });
        this.getCategories();
    }

    onChangeCategory(value) {
        this.tagsValue = []
        this.tags = this.cats.find(x => x.id === value).tags;
        this.areTagsAvailable = this.tags.length > 0;
    }

    getCategories() {
        this.cat.all().subscribe(
            c => { this.cats = c; },
            error => this.message = <any>error);
    }

    send() {
        this.submitted = true;
        this.myForm.controls["tags"].setValue(this.tagsValue.map((item: any) => { return item.id; }).join(","));
        this.work.create(this.myForm.value).subscribe(
            () => {
                this.router.navigate(["/dashboard"]);
            },
            (res) => {
                this.message = res;
            }
        )
    }

    public refreshValue(value: any): void {
        this.tagsValue = value;
    }

    // public selected(value: any): void {
    //     console.log("Selected value is: ", value);
    //     console.log(this.myForm.value);
    //     console.log(this.tagsValue);
    // }

    // public removed(value: any): void {
    //     console.log("Removed value is: ", value);
    // }

    // public typed(value: any): void {
    //     console.log("New input: ", value);
    // }

    // public itemsToString(value: Array<any> = []): string {
    //     return value
    //         .map((item: any) => {
    //             return item.text;
    //         }).join(",");
    // }
}