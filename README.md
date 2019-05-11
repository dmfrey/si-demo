# Spring Integration Demo

```bash
./mvnw clean package
```

I recreated the issue.

After thinking about it more, it happens because the JpaPollingChannelAdapter is marked as `@MockBean`.
But it only fails when the `@EnableTransactionManagement` is enabled.

So maybe the best course of action here is to just move that annotation off to another profile,
or only one that runs when the cloud profile is activated.