package hr.fer.rassus.lab1.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.41.0)",
    comments = "Source: readings.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReadingsServiceGrpc {

  private ReadingsServiceGrpc() {}

  public static final String SERVICE_NAME = "hr.fer.rassus.lab1.grpc.ReadingsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<hr.fer.rassus.lab1.grpc.ReadingRequest,
      hr.fer.rassus.lab1.grpc.ReadingResponse> getGetReadingsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetReadings",
      requestType = hr.fer.rassus.lab1.grpc.ReadingRequest.class,
      responseType = hr.fer.rassus.lab1.grpc.ReadingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<hr.fer.rassus.lab1.grpc.ReadingRequest,
      hr.fer.rassus.lab1.grpc.ReadingResponse> getGetReadingsMethod() {
    io.grpc.MethodDescriptor<hr.fer.rassus.lab1.grpc.ReadingRequest, hr.fer.rassus.lab1.grpc.ReadingResponse> getGetReadingsMethod;
    if ((getGetReadingsMethod = ReadingsServiceGrpc.getGetReadingsMethod) == null) {
      synchronized (ReadingsServiceGrpc.class) {
        if ((getGetReadingsMethod = ReadingsServiceGrpc.getGetReadingsMethod) == null) {
          ReadingsServiceGrpc.getGetReadingsMethod = getGetReadingsMethod =
              io.grpc.MethodDescriptor.<hr.fer.rassus.lab1.grpc.ReadingRequest, hr.fer.rassus.lab1.grpc.ReadingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetReadings"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  hr.fer.rassus.lab1.grpc.ReadingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  hr.fer.rassus.lab1.grpc.ReadingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ReadingsServiceMethodDescriptorSupplier("GetReadings"))
              .build();
        }
      }
    }
    return getGetReadingsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReadingsServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceStub>() {
        @java.lang.Override
        public ReadingsServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReadingsServiceStub(channel, callOptions);
        }
      };
    return ReadingsServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReadingsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceBlockingStub>() {
        @java.lang.Override
        public ReadingsServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReadingsServiceBlockingStub(channel, callOptions);
        }
      };
    return ReadingsServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReadingsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReadingsServiceFutureStub>() {
        @java.lang.Override
        public ReadingsServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReadingsServiceFutureStub(channel, callOptions);
        }
      };
    return ReadingsServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class ReadingsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getReadings(hr.fer.rassus.lab1.grpc.ReadingRequest request,
        io.grpc.stub.StreamObserver<hr.fer.rassus.lab1.grpc.ReadingResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetReadingsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetReadingsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                hr.fer.rassus.lab1.grpc.ReadingRequest,
                hr.fer.rassus.lab1.grpc.ReadingResponse>(
                  this, METHODID_GET_READINGS)))
          .build();
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReadingsServiceStub extends io.grpc.stub.AbstractAsyncStub<ReadingsServiceStub> {
    private ReadingsServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReadingsServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReadingsServiceStub(channel, callOptions);
    }

    /**
     */
    public void getReadings(hr.fer.rassus.lab1.grpc.ReadingRequest request,
        io.grpc.stub.StreamObserver<hr.fer.rassus.lab1.grpc.ReadingResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetReadingsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReadingsServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<ReadingsServiceBlockingStub> {
    private ReadingsServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReadingsServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReadingsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public hr.fer.rassus.lab1.grpc.ReadingResponse getReadings(hr.fer.rassus.lab1.grpc.ReadingRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetReadingsMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class ReadingsServiceFutureStub extends io.grpc.stub.AbstractFutureStub<ReadingsServiceFutureStub> {
    private ReadingsServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReadingsServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReadingsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<hr.fer.rassus.lab1.grpc.ReadingResponse> getReadings(
        hr.fer.rassus.lab1.grpc.ReadingRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetReadingsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_READINGS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ReadingsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ReadingsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_READINGS:
          serviceImpl.getReadings((hr.fer.rassus.lab1.grpc.ReadingRequest) request,
              (io.grpc.stub.StreamObserver<hr.fer.rassus.lab1.grpc.ReadingResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ReadingsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReadingsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return hr.fer.rassus.lab1.grpc.ReadingsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReadingsService");
    }
  }

  private static final class ReadingsServiceFileDescriptorSupplier
      extends ReadingsServiceBaseDescriptorSupplier {
    ReadingsServiceFileDescriptorSupplier() {}
  }

  private static final class ReadingsServiceMethodDescriptorSupplier
      extends ReadingsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ReadingsServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ReadingsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReadingsServiceFileDescriptorSupplier())
              .addMethod(getGetReadingsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
