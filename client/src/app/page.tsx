import { IPost } from "./model/post.interface";
import nextConfig from "../../next.config";
import PostCardProps from "./components/PostCard";

export default async function Home() {
  const res = await fetch(`${nextConfig.apiURL}/posts`);
  let data: IPost[] = await res.json();

  return (
    <div className="grid grid-cols-3 gap-3 p-10">
      {data.map((p) => (
        <PostCardProps key={p.id} post={p}></PostCardProps>
      ))}
    </div>
  );
}
