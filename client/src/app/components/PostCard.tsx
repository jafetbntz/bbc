import Link from "next/link";
import { IPost } from "../model/post.interface";
import { formatDistance } from "date-fns";

export type PostCardPropsProps = {
    post: IPost
}
  
  export default function PostCardProps( {post}: PostCardPropsProps) {
        const publishingDate = post.createDate ? formatDistance(post.createDate, new Date(), { addSuffix: true }): null;
        return (
            <div className="card bg-base-100 card-xs shadow-sm p-1">
            <div className="card-body">
              <h2 className="card-title">{post.title}</h2>
              {
                              (
                                publishingDate &&
                                <p className="text-gray italic">{publishingDate}</p>
                              )
              }
              <p>
                {post.content}
              </p>
              <div className="justify-end card-actions">
                <Link href={`/posts/${post.id}`} className="btn btn-ghost">Read more</Link>

                {(post.slug && <Link href={`/live/${post.slug}`} className="btn btn-primary">Go live</Link>)}
                
              </div>
            </div>


          </div>
        );

  }