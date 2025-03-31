"use client";

import { IPost } from "@/app/model/post.interface";
import { useEffect, useState } from "react";
import nextConfig from "../../../../next.config";

export type LiveNewsPageProps = {
  params: {
    slug: string;
  };
};

export default function LiveNewsPage({ params }: LiveNewsPageProps) {

  const [data, setData] = useState([] as IPost[]);
  let eventSource: EventSource | undefined = undefined;
  useEffect(() => {
    const { slug } = params;

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


    return () => {
        eventSource?.close();

    }
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
