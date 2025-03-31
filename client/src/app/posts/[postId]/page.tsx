import { IPost } from "@/app/model/post.interface";
import nextConfig from "../../../../next.config";
import { formatDistance } from "date-fns";



export type PostDetailPageProps = {
    params: {
        postId: string;
    }
};

export default async function PostDetailPage({params}: PostDetailPageProps) {
    const { postId } = await params;

    const res = await fetch(`${nextConfig.apiURL}/posts/${postId}`);
    let post: IPost = await res.json();

    const publishingDate = post.createDate ? formatDistance(post.createDate, new Date(), { addSuffix: true }): null;
    

    return (
        <div>

            <div className="p-20">
                <h1 className="text-2xl text-bold">{post.title}</h1>
                <hr/>
                {
                    (
                    publishingDate &&
                    <p className="text-gray italic">{publishingDate}</p>
                    )
              }
                <br/>
                <p>{post.content}</p>

            </div>

        </div>
    )
}

