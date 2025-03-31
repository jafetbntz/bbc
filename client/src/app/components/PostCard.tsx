import Link from "next/link";
import { IPost } from "../model/post.interface";

export type PostCardPropsProps = {
    post: IPost
}
  
  export default function PostCardProps( {post}: PostCardPropsProps) {

        return (
            <div className="card bg-base-100 card-xs shadow-sm">
            <div className="card-body">
              <h2 className="card-title">{post.title}</h2>
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