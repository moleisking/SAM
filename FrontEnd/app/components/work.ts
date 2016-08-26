import { Component, OnInit, OnDestroy } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

import { WorkService } from "../services/work";
import { CategoriesService } from "../services/categories";
import { WorkModel } from "../models/work";
import { TagModel } from "../models/tag";

@Component({
    selector: "work-component",
    templateUrl: "/views/work.html",
    providers: [WorkService]
})

export class Work implements OnInit, OnDestroy {

    private sub: any;
    private message: string;
    private workModel: WorkModel;

    constructor(private route: ActivatedRoute, private work: WorkService, private cat: CategoriesService) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            let id = params["id"];
            let username = params["username"];
            this.work.getWork(username, id).subscribe(
                work => {
                    this.workModel = work;
                    this.cat.all().subscribe(
                        cats => {
                            let cat = cats.filter(c => c.id === this.workModel.category)[0];
                            this.workModel.categoryName = cat.name;
                            this.workModel.tagsObject = new Array<TagModel>();
                            if (this.workModel.tags.indexOf(",") < 0)
                                this.workModel.tagsObject.push(cat.tags.filter(t => t.id === parseInt(this.workModel.tags))[0]);
                            else
                                this.workModel.tags.split(",").forEach(item => {
                                    this.workModel.tagsObject.push(cat.tags.filter(t => t.id === parseInt(item))[0]);
                                });
                        },
                        error => this.message = <any>error
                    );
                },
                error => this.message = <any>error
            );
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}