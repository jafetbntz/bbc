"use client";
import { IPost } from "@/app/model/post.interface";
import { use, useEffect, useState } from "react";
import nextConfig from "../../../../next.config";

export type LiveNewsPageProps = {
  params: Promise<{
    slug: string;
  }>;
};

export default function LiveNewsPage({ params }: LiveNewsPageProps) {
  const { slug } = use(params);

  const [data, setData] = useState([] as IPost[]);
  let eventSource: EventSource | undefined = undefined;

  useEffect(() => {
    fetch(`${nextConfig.apiURL}/posts/live/${slug}`)
      .then(async (res) => {
        let previousPosts: IPost[] = await res.json();
        setData(previousPosts);
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

          setData((prev) => {
            return [value, ...prev];
          });
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
      {data.map((p) => (
        <p>
          <b>
            {p.title} : {p.content}
          </b>
        </p>
      ))}
    </div>
  );
}
