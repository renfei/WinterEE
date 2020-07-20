FROM elasticsearch:7.8.0
ENV WORK_PATH /usr/share/elasticsearch
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ  /etc/localtime && echo $TZ > /etc/timezone
ADD ./elasticsearch.yml $WORK_PATH/config
RUN mkdir -p $WORK_PATH/plugins/ik
ADD ./elasticsearch-analysis-ik-7.8.0.zip $WORK_PATH/plugins/ik
RUN cd $WORK_PATH/plugins/ik && unzip elasticsearch-analysis-ik-7.8.0.zip && rm -rf elasticsearch-analysis-ik-7.8.0.zip