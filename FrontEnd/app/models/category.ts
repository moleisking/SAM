import { Tag } from "./tag";

export interface Category {
    id: number;
    name: string;
    description: string;
    tags: Array<Tag>
}