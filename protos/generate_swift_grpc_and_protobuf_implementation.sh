protoc clothing_webshop.proto \
     --proto_path=./kotlinclothingwebshop/ \
     --grpc-swift_opt=Client=true,Server=false,Visibility=Public \
     --grpc-swift_out=. \
     --swift_out=. \
     --swift_opt=Visibility=Public