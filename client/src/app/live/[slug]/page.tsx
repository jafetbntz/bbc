"use client";
import { IPost } from "@/app/model/post.interface";
import { use, useEffect, useState } from "react";
import nextConfig from "../../../../next.config";
import PostCard from "@/app/components/PostCard";

export type LiveNewsPageProps = {
  params: Promise<{
    slug: string;
  }>;
};

export default function LiveNewsPage({ params }: LiveNewsPageProps) {
  const { slug } = use(params);

  const [postIds, setPostIds] = useState([] as string[]);
  const [data, setData] = useState([] as IPost[]);
  let eventSource: EventSource | undefined = undefined;

  useEffect(() => {
    fetch(`${nextConfig.apiURL}/posts/live/${slug}`)
      .then(async (res) => {
        let previousPosts: IPost[] = await res.json();
        setData(previousPosts);
        setPostIds(previousPosts.map(p => p.id));
      })
      .catch((error) => {
        console.error("Issues fetching previous post");
        console.error("🚀 ~ useEffect ~ error:", error);
      })
      .finally(() => {

        eventSource = new EventSource(
          `${nextConfig.apiURL}/posts/stream/${slug}`
        );
        eventSource.onmessage = (event) => {
          const value = JSON.parse(event.data);

          if (postIds.indexOf(value.id) == -1) {
            setData((prev) => {
                return [value, ...prev];
              });
    
              setPostIds((prev) => {
                return [value.id, ...prev];
              });
          }


        };
        eventSource.onerror = (err) => {
          console.error("EventSource failed:", err);
          eventSource?.close();
        };
      });

    return () => {
      eventSource?.close();
    };
  }, []);

  return (
    <div>
      <div className="h-30 bg-black text-white content-center">
        <h1 className="text-center">{slug}</h1>
      </div>
      <div className="grid grid-cols-1 gap-1 p-20">
        {data.map((p) => (
          <PostCard post={p} key={p.id}></PostCard>
        ))}
      </div>
    </div>
  );
}
