import { TagModel } from "./tag";

export interface CategoryModel {
    id: number;
    name: string;
    description: string;
    tags: Array<TagModel>
}