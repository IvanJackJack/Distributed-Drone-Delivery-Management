package project.drone.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: DroneService.proto")
public final class DroneServiceGrpc {

  private DroneServiceGrpc() {}

  public static final String SERVICE_NAME = "project.drone.grpc.DroneService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.JoinNotification,
      project.drone.grpc.DroneServiceOuterClass.JoinResponse> getJoinMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "join",
      requestType = project.drone.grpc.DroneServiceOuterClass.JoinNotification.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.JoinResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.JoinNotification,
      project.drone.grpc.DroneServiceOuterClass.JoinResponse> getJoinMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.JoinNotification, project.drone.grpc.DroneServiceOuterClass.JoinResponse> getJoinMethod;
    if ((getJoinMethod = DroneServiceGrpc.getJoinMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getJoinMethod = DroneServiceGrpc.getJoinMethod) == null) {
          DroneServiceGrpc.getJoinMethod = getJoinMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.JoinNotification, project.drone.grpc.DroneServiceOuterClass.JoinResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "join"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.JoinNotification.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.JoinResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("join"))
              .build();
        }
      }
    }
    return getJoinMethod;
  }

  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.ElectionMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getElectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "election",
      requestType = project.drone.grpc.DroneServiceOuterClass.ElectionMessage.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.ElectionMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getElectionMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.ElectionMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse> getElectionMethod;
    if ((getElectionMethod = DroneServiceGrpc.getElectionMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getElectionMethod = DroneServiceGrpc.getElectionMethod) == null) {
          DroneServiceGrpc.getElectionMethod = getElectionMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.ElectionMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "election"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.ElectionMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("election"))
              .build();
        }
      }
    }
    return getElectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.InfoMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateInfo",
      requestType = project.drone.grpc.DroneServiceOuterClass.InfoMessage.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.InfoMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateInfoMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.InfoMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateInfoMethod;
    if ((getUpdateInfoMethod = DroneServiceGrpc.getUpdateInfoMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getUpdateInfoMethod = DroneServiceGrpc.getUpdateInfoMethod) == null) {
          DroneServiceGrpc.getUpdateInfoMethod = getUpdateInfoMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.InfoMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.InfoMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("updateInfo"))
              .build();
        }
      }
    }
    return getUpdateInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.OrderMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getSendOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendOrder",
      requestType = project.drone.grpc.DroneServiceOuterClass.OrderMessage.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.OrderMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getSendOrderMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.OrderMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse> getSendOrderMethod;
    if ((getSendOrderMethod = DroneServiceGrpc.getSendOrderMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getSendOrderMethod = DroneServiceGrpc.getSendOrderMethod) == null) {
          DroneServiceGrpc.getSendOrderMethod = getSendOrderMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.OrderMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendOrder"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.OrderMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("sendOrder"))
              .build();
        }
      }
    }
    return getSendOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.DeliveryMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateDeliveryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "updateDelivery",
      requestType = project.drone.grpc.DroneServiceOuterClass.DeliveryMessage.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.DeliveryMessage,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateDeliveryMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.DeliveryMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse> getUpdateDeliveryMethod;
    if ((getUpdateDeliveryMethod = DroneServiceGrpc.getUpdateDeliveryMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getUpdateDeliveryMethod = DroneServiceGrpc.getUpdateDeliveryMethod) == null) {
          DroneServiceGrpc.getUpdateDeliveryMethod = getUpdateDeliveryMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.DeliveryMessage, project.drone.grpc.DroneServiceOuterClass.AckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "updateDelivery"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.DeliveryMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("updateDelivery"))
              .build();
        }
      }
    }
    return getUpdateDeliveryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.AckResponse,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getPingPongMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "pingPong",
      requestType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      responseType = project.drone.grpc.DroneServiceOuterClass.AckResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.AckResponse,
      project.drone.grpc.DroneServiceOuterClass.AckResponse> getPingPongMethod() {
    io.grpc.MethodDescriptor<project.drone.grpc.DroneServiceOuterClass.AckResponse, project.drone.grpc.DroneServiceOuterClass.AckResponse> getPingPongMethod;
    if ((getPingPongMethod = DroneServiceGrpc.getPingPongMethod) == null) {
      synchronized (DroneServiceGrpc.class) {
        if ((getPingPongMethod = DroneServiceGrpc.getPingPongMethod) == null) {
          DroneServiceGrpc.getPingPongMethod = getPingPongMethod =
              io.grpc.MethodDescriptor.<project.drone.grpc.DroneServiceOuterClass.AckResponse, project.drone.grpc.DroneServiceOuterClass.AckResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "pingPong"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  project.drone.grpc.DroneServiceOuterClass.AckResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DroneServiceMethodDescriptorSupplier("pingPong"))
              .build();
        }
      }
    }
    return getPingPongMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DroneServiceStub newStub(io.grpc.Channel channel) {
    return new DroneServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DroneServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DroneServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DroneServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DroneServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class DroneServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void join(project.drone.grpc.DroneServiceOuterClass.JoinNotification request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getJoinMethod(), responseObserver);
    }

    /**
     */
    public void election(project.drone.grpc.DroneServiceOuterClass.ElectionMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getElectionMethod(), responseObserver);
    }

    /**
     */
    public void updateInfo(project.drone.grpc.DroneServiceOuterClass.InfoMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateInfoMethod(), responseObserver);
    }

    /**
     */
    public void sendOrder(project.drone.grpc.DroneServiceOuterClass.OrderMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendOrderMethod(), responseObserver);
    }

    /**
     */
    public void updateDelivery(project.drone.grpc.DroneServiceOuterClass.DeliveryMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateDeliveryMethod(), responseObserver);
    }

    /**
     */
    public void pingPong(project.drone.grpc.DroneServiceOuterClass.AckResponse request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPingPongMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getJoinMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.JoinNotification,
                project.drone.grpc.DroneServiceOuterClass.JoinResponse>(
                  this, METHODID_JOIN)))
          .addMethod(
            getElectionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.ElectionMessage,
                project.drone.grpc.DroneServiceOuterClass.AckResponse>(
                  this, METHODID_ELECTION)))
          .addMethod(
            getUpdateInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.InfoMessage,
                project.drone.grpc.DroneServiceOuterClass.AckResponse>(
                  this, METHODID_UPDATE_INFO)))
          .addMethod(
            getSendOrderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.OrderMessage,
                project.drone.grpc.DroneServiceOuterClass.AckResponse>(
                  this, METHODID_SEND_ORDER)))
          .addMethod(
            getUpdateDeliveryMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.DeliveryMessage,
                project.drone.grpc.DroneServiceOuterClass.AckResponse>(
                  this, METHODID_UPDATE_DELIVERY)))
          .addMethod(
            getPingPongMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                project.drone.grpc.DroneServiceOuterClass.AckResponse,
                project.drone.grpc.DroneServiceOuterClass.AckResponse>(
                  this, METHODID_PING_PONG)))
          .build();
    }
  }

  /**
   */
  public static final class DroneServiceStub extends io.grpc.stub.AbstractStub<DroneServiceStub> {
    private DroneServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneServiceStub(channel, callOptions);
    }

    /**
     */
    public void join(project.drone.grpc.DroneServiceOuterClass.JoinNotification request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.JoinResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void election(project.drone.grpc.DroneServiceOuterClass.ElectionMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateInfo(project.drone.grpc.DroneServiceOuterClass.InfoMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendOrder(project.drone.grpc.DroneServiceOuterClass.OrderMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDelivery(project.drone.grpc.DroneServiceOuterClass.DeliveryMessage request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateDeliveryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pingPong(project.drone.grpc.DroneServiceOuterClass.AckResponse request,
        io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingPongMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DroneServiceBlockingStub extends io.grpc.stub.AbstractStub<DroneServiceBlockingStub> {
    private DroneServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.JoinResponse join(project.drone.grpc.DroneServiceOuterClass.JoinNotification request) {
      return blockingUnaryCall(
          getChannel(), getJoinMethod(), getCallOptions(), request);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.AckResponse election(project.drone.grpc.DroneServiceOuterClass.ElectionMessage request) {
      return blockingUnaryCall(
          getChannel(), getElectionMethod(), getCallOptions(), request);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.AckResponse updateInfo(project.drone.grpc.DroneServiceOuterClass.InfoMessage request) {
      return blockingUnaryCall(
          getChannel(), getUpdateInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.AckResponse sendOrder(project.drone.grpc.DroneServiceOuterClass.OrderMessage request) {
      return blockingUnaryCall(
          getChannel(), getSendOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.AckResponse updateDelivery(project.drone.grpc.DroneServiceOuterClass.DeliveryMessage request) {
      return blockingUnaryCall(
          getChannel(), getUpdateDeliveryMethod(), getCallOptions(), request);
    }

    /**
     */
    public project.drone.grpc.DroneServiceOuterClass.AckResponse pingPong(project.drone.grpc.DroneServiceOuterClass.AckResponse request) {
      return blockingUnaryCall(
          getChannel(), getPingPongMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DroneServiceFutureStub extends io.grpc.stub.AbstractStub<DroneServiceFutureStub> {
    private DroneServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DroneServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DroneServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DroneServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.JoinResponse> join(
        project.drone.grpc.DroneServiceOuterClass.JoinNotification request) {
      return futureUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.AckResponse> election(
        project.drone.grpc.DroneServiceOuterClass.ElectionMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getElectionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.AckResponse> updateInfo(
        project.drone.grpc.DroneServiceOuterClass.InfoMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.AckResponse> sendOrder(
        project.drone.grpc.DroneServiceOuterClass.OrderMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getSendOrderMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.AckResponse> updateDelivery(
        project.drone.grpc.DroneServiceOuterClass.DeliveryMessage request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateDeliveryMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<project.drone.grpc.DroneServiceOuterClass.AckResponse> pingPong(
        project.drone.grpc.DroneServiceOuterClass.AckResponse request) {
      return futureUnaryCall(
          getChannel().newCall(getPingPongMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_JOIN = 0;
  private static final int METHODID_ELECTION = 1;
  private static final int METHODID_UPDATE_INFO = 2;
  private static final int METHODID_SEND_ORDER = 3;
  private static final int METHODID_UPDATE_DELIVERY = 4;
  private static final int METHODID_PING_PONG = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DroneServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DroneServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_JOIN:
          serviceImpl.join((project.drone.grpc.DroneServiceOuterClass.JoinNotification) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.JoinResponse>) responseObserver);
          break;
        case METHODID_ELECTION:
          serviceImpl.election((project.drone.grpc.DroneServiceOuterClass.ElectionMessage) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse>) responseObserver);
          break;
        case METHODID_UPDATE_INFO:
          serviceImpl.updateInfo((project.drone.grpc.DroneServiceOuterClass.InfoMessage) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse>) responseObserver);
          break;
        case METHODID_SEND_ORDER:
          serviceImpl.sendOrder((project.drone.grpc.DroneServiceOuterClass.OrderMessage) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse>) responseObserver);
          break;
        case METHODID_UPDATE_DELIVERY:
          serviceImpl.updateDelivery((project.drone.grpc.DroneServiceOuterClass.DeliveryMessage) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse>) responseObserver);
          break;
        case METHODID_PING_PONG:
          serviceImpl.pingPong((project.drone.grpc.DroneServiceOuterClass.AckResponse) request,
              (io.grpc.stub.StreamObserver<project.drone.grpc.DroneServiceOuterClass.AckResponse>) responseObserver);
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

  private static abstract class DroneServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DroneServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return project.drone.grpc.DroneServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DroneService");
    }
  }

  private static final class DroneServiceFileDescriptorSupplier
      extends DroneServiceBaseDescriptorSupplier {
    DroneServiceFileDescriptorSupplier() {}
  }

  private static final class DroneServiceMethodDescriptorSupplier
      extends DroneServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DroneServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (DroneServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DroneServiceFileDescriptorSupplier())
              .addMethod(getJoinMethod())
              .addMethod(getElectionMethod())
              .addMethod(getUpdateInfoMethod())
              .addMethod(getSendOrderMethod())
              .addMethod(getUpdateDeliveryMethod())
              .addMethod(getPingPongMethod())
              .build();
        }
      }
    }
    return result;
  }
}
