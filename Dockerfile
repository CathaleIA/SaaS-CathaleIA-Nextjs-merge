
FROM public.ecr.aws/amazonlinux/amazonlinux:2023

# 1. Instalar herramientas base
RUN dnf update -y && \
    dnf install -y \
    shadow-utils \
    sudo \
    git \
    unzip \
    wget \
    tar \
    gzip \
    findutils \
    gcc \
    make \
    openssl-devel \
    bzip2-devel \
    libffi-devel \
    zlib-devel \
    sqlite-devel \
    python3.12 \
    python3.12-devel \
    jq && \
    dnf clean all

# 2. Instalar curl completo
RUN dnf install -y curl --allowerasing && \
    dnf clean all

# 3. Instalar Node.js 20.x y npm desde NodeSource
RUN curl -fsSL https://rpm.nodesource.com/setup_20.x | bash - && \
    dnf install -y nodejs && \
    dnf clean all

# 4. Configurar usuario (ec2-user con permisos)
RUN useradd -m -u 1000 -s /bin/bash ec2-user && \
    echo 'ec2-user ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

# 5. Configurar Python 3.12 como predeterminado
RUN alternatives --install /usr/bin/python3 python3 /usr/bin/python3.12 1 && \
    alternatives --set python3 /usr/bin/python3.12 && \
    python3 -m ensurepip --upgrade && \
    ln -sf /usr/bin/pip3.12 /usr/bin/pip3

# 6. Instalar dependencias globales de Python
RUN python3 -m pip install --upgrade pip setuptools wheel && \
    python3 -m pip install \
    aws_lambda_powertools \
    requests \
    pylint \
    boto3 \
    git-remote-codecommit

# 7. AWS CLI v2
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install --bin-dir /usr/local/bin && \
    rm -rf awscliv2.zip aws

# 8. SAM CLI
RUN wget https://github.com/aws/aws-sam-cli/releases/latest/download/aws-sam-cli-linux-x86_64.zip -O aws-sam-cli.zip && \
    unzip aws-sam-cli.zip -d sam-installation && \
    ./sam-installation/install && \
    rm -rf aws-sam-cli.zip sam-installation

# 9. Instalar AWS CDK globalmente
RUN npm install -g aws-cdk

# 10. Configurar entorno de trabajo
USER ec2-user
WORKDIR /home/ec2-user/app

# 11. Copiar archivos
COPY --chown=ec2-user:ec2-user . .

# 12. Verificar instalaciones (opcional)
RUN node --version && \
    npm --version && \
    cdk --version