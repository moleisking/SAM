import { TagModel } from "../models/tag";

export interface WorkModel {
    name: string;
    nameurl: string;
    username: string;
    description: string;
    category: number;
    categoryName: string;
    tags: string;
    tagsObject: Array<TagModel>;
}