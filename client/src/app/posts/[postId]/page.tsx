import { IPost } from "@/app/model/post.interface";
import nextConfig from "../../../../next.config";



export type PostDetailPageProps = {
    params: {
        postId: string;
    }
};

export default async function PostDetailPage({params}: PostDetailPageProps) {
    const { postId } = await params;

    const res = await fetch(`${nextConfig.apiURL}/posts/${postId}`);
    let data: IPost = await res.json();
    return (
        <div>
                    <h1>{data.title}</h1>
                    <p>{data.content}</p>
        </div>
    )
}

